import {AfterViewInit, Component, EventEmitter, Input, Output,} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {
  AdminTestCommentRequest,
  AdminTestCommentResponse,
  Choice,
  QuestionResponse,
  ScoringStrategyResponse,
  TestTemplateResponse,
  TestTemplateUpdateRequest,
} from 'src/app/services/models';
import {CommentAdminService, QuestionAdminService, ScoringAdminService, TestTemplateAdminService} from 'src/app/services/services';

@Component({
  selector: 'app-test-card-detail',
  templateUrl: './test-card-detail.component.html',
  styleUrls: ['./test-card-detail.component.scss'],
})
export class TestCardDetailComponent implements AfterViewInit {
  @Input() testCardList: TestTemplateResponse[] = [];
  @Input() isEditPage: boolean = false;

  cardId: number = this.route.snapshot.params['id'];
  @Input() editableTestTemplate: TestTemplateResponse | null = null;

  @Output() closeModalEvent = new EventEmitter<void>();

  updateTestTemplate: TestTemplateUpdateRequest = {} as TestTemplateUpdateRequest;

  testTemplate: TestTemplateResponse | null = null;

  testResult: any = null;
  testCard: TestTemplateResponse | null = null;

  title: string = 'Psikolojik Testler';
  text: string =
    'Kendinizi daha iyi tanımak için tasarlanmış çeşitli psikolojik testlerimizi deneyebilirsiniz. Bu testler kişilik özellikleri, duygusal durum ve davranış kalıplarınız hakkında içgörü kazanmanıza yardımcı olacaktır.';

  selectedFile: File | null = null;
  @Input() imageUrl: string = '';

  // Question management for test templates
  templateQuestions: QuestionResponse[] = [];
  showQuestionManagement: boolean = false;

  // Comment management for test templates
  templateComments: AdminTestCommentResponse[] = [];
  showCommentManagement: boolean = false;

  // Strategy management
  availableStrategies: ScoringStrategyResponse[] = [];
  selectedStrategy: string | null = null;

  // Toast messages
  toastMessage: string = '';
  toastType: 'success' | 'error' = 'error';
  showToast: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private testTemplateAdminService: TestTemplateAdminService,
    private questionAdminService: QuestionAdminService,
    private scoringAdminService: ScoringAdminService,
    private commentAdminService: CommentAdminService
  ) {
    this.cardId = this.route.snapshot.params['id'];
  }

  ngAfterViewInit() {
    if (!this.editableTestTemplate && !this.isEditPage) {
      this.getTestTemplateDetail();
    }

    if (this.editableTestTemplate) {
      this.updateTestTemplate = {
        title: this.editableTestTemplate?.title || '',
        subTitle: this.editableTestTemplate?.subTitle,
        isActive: this.editableTestTemplate?.isActive,
        scoringStrategy: this.editableTestTemplate?.scoringStrategy,
      };
      this.selectedStrategy = this.editableTestTemplate?.scoringStrategy || null;
      this.testCard = this.editableTestTemplate;
      // Set imageUrl for display purposes only, don't include in update request
      if (this.editableTestTemplate?.imageUrl) {
        this.imageUrl = this.editableTestTemplate.imageUrl;
      }
    }

    // Load available strategies
    this.loadStrategies();
  }

  getTestTemplateDetail() {
    this.testTemplateAdminService.getTestTemplateById1({ id: this.cardId }).subscribe({
      next: (template: TestTemplateResponse) => {
        this.testTemplate = template;
        this.testCard = template;
      },
      error: (err: any) => {
        console.error('Error fetching test template', err);
      },
    });
  }

  saveOrUpdateTestInfo() {
    this.saveOrUpdateTestTemplateInfo();
  }

  saveAllChanges() {
    // Update the scoring strategy in the update request
    if (this.selectedStrategy) {
      this.updateTestTemplate.scoringStrategy = this.selectedStrategy as 'SIMPLE_LINEAR' | 'WEIGHTED' | 'PERCENTAGE';
    }

    // Handle imageUrl based on whether a new image is selected
    if (!this.selectedFile && this.editableTestTemplate?.imagePath) {
      // No new image selected - use imagePath (raw filename) to prevent host duplication
      this.updateTestTemplate.imageUrl = this.editableTestTemplate.imagePath;
    } else if (this.selectedFile) {
      // New image selected - don't include imageUrl in update, let upload handle it
      delete this.updateTestTemplate.imageUrl;
    }

    if (this.editableTestTemplate?.id) {
      // UPDATE existing template
      const params = {
        id: this.editableTestTemplate.id,
        body: this.updateTestTemplate,
      };
      this.testTemplateAdminService.updateTestTemplate(params).subscribe({
        next: (response: TestTemplateResponse) => {
          // After successful template update, upload image if selected
          if (this.selectedFile && this.editableTestTemplate?.id) {
            this.uploadSelectedImage();
          } else {
            this.closeModalEvent.emit();
          }
        },
        error: (err: any) => {
          console.error('Error updating test template', err);
        },
      });
    } else {
      // CREATE new template
      const params = {
        body: this.updateTestTemplate,
      };
      this.testTemplateAdminService.createTestTemplate(params).subscribe({
        next: (response: TestTemplateResponse) => {
          // After successful template creation, upload image if selected
          if (this.selectedFile && response.id) {
            this.uploadImageForNewTemplate(response.id);
          } else {
            this.closeModalEvent.emit();
          }
        },
        error: (err: any) => {
          console.error('Error creating test template', err);
        },
      });
    }
  }

  saveOrUpdateTestTemplateInfo() {
    if (this.editableTestTemplate?.id) {
      // UPDATE existing template
      const params = {
        id: this.editableTestTemplate.id,
        body: this.updateTestTemplate,
      };
      this.testTemplateAdminService.updateTestTemplate(params).subscribe({
        next: (response: TestTemplateResponse) => {
          this.closeModalEvent.emit();
        },
        error: (err: any) => {
          console.error('Error updating test template', err);
        },
      });
    } else {
      // CREATE new template
      this.saveTestTemplate();
    }
  }

  saveTestTemplate() {
    const params = {
      body: this.updateTestTemplate,
    };
    this.testTemplateAdminService.createTestTemplate(params).subscribe({
      next: (response: TestTemplateResponse) => {
        this.closeModalEvent.emit();
      },
      error: (err: any) => {
        console.error('Error creating test template', err);
      },
    });
  }

  navigateToQuestionManagement() {
    if (this.editableTestTemplate?.id) {
      this.loadTemplateQuestions(this.editableTestTemplate.id);
      this.showQuestionManagement = true;
    } else {
      // Yeni test için önce template'i kaydet
      this.saveTestTemplateForQuestionManagement();
    }
  }

  saveTestTemplateForQuestionManagement() {
    const params = {
      body: this.updateTestTemplate,
    };
    this.testTemplateAdminService.createTestTemplate(params).subscribe({
      next: (response: TestTemplateResponse) => {
        // Kaydedilen template'i güncelle
        this.editableTestTemplate = response;
        this.testCard = response;
        // Soru yönetimi sayfasına geç
        if (response.id) {
          this.loadTemplateQuestions(response.id);
          this.showQuestionManagement = true;
        }
      },
      error: (err: any) => {
        console.error('Error creating test template for question management', err);
        this.showToast = true;
        this.toastType = 'error';
        this.toastMessage = 'Test kaydedilirken bir hata oluştu. Lütfen tekrar deneyin.';
      },
    });
  }

  loadTemplateQuestions(testTemplateId: number) {
    this.questionAdminService.getQuestionsByTestTemplate({ testTemplateId }).subscribe({
      next: (questions: QuestionResponse[]) => {
        this.templateQuestions = questions || [];
      },
      error: (err: any) => {
        console.error('Error loading template questions', err);
      },
    });
  }

  addTemplateQuestion() {
    const newQuestion: QuestionResponse = {
      text: '',
      testTemplateId: this.editableTestTemplate?.id,
      orderIndex: this.templateQuestions.length,
      choices: [
        { text: '', answerType: 'ANSWER_A' },
        { text: '', answerType: 'ANSWER_B' },
        { text: '', answerType: 'ANSWER_C' },
        { text: '', answerType: 'ANSWER_D' },
        { text: '', answerType: 'ANSWER_E' },
      ],
    };
    this.templateQuestions.push(newQuestion);
  }

  removeTemplateQuestion(index: number) {
    this.templateQuestions.splice(index, 1);
  }

  addTemplateChoice(questionIndex: number) {
    const question = this.templateQuestions[questionIndex];
    if (question.choices && question.choices.length < 5) {
      const nextAnswerType = ['ANSWER_A', 'ANSWER_B', 'ANSWER_C', 'ANSWER_D', 'ANSWER_E'][question.choices.length] as Choice['answerType'];
      question.choices.push({
        text: '',
        answerType: nextAnswerType,
      });
    }
  }

  removeTemplateChoice(questionIndex: number, choiceIndex: number) {
    const question = this.templateQuestions[questionIndex];
    if (question.choices && question.choices.length > 2) {
      question.choices.splice(choiceIndex, 1);
    }
  }

  saveTemplateQuestions() {
    if (!this.editableTestTemplate?.id) {
      this.showToast = true;
      this.toastType = 'error';
      this.toastMessage = 'Test template ID is missing';
      return;
    }

    this.testTemplateAdminService.updateTestTemplateQuestions({
      id: this.editableTestTemplate.id,
      body: this.templateQuestions
    }).subscribe({
      next: (updatedQuestions) => {
        this.templateQuestions = updatedQuestions;
        this.showQuestionManagement = false;
        this.showToast = true;
        this.toastType = 'success';
        this.toastMessage = 'Sorular başarıyla güncellendi!';
      },
      error: (error) => {
        console.error('Error updating questions:', error);
        this.showToast = true;
        this.toastType = 'error';
        this.toastMessage = 'Sorular güncellenirken bir hata oluştu. Lütfen tekrar deneyin.';
      }
    });
  }

  navigateToCommentManagement() {
    if (this.editableTestTemplate?.id) {
      this.loadTemplateComments(this.editableTestTemplate.id);
      this.showCommentManagement = true;
    } else {
      // Yeni test için önce template'i kaydet
      this.saveTestTemplateForCommentManagement();
    }
  }

  saveTestTemplateForCommentManagement() {
    const params = {
      body: this.updateTestTemplate,
    };
    this.testTemplateAdminService.createTestTemplate(params).subscribe({
      next: (response: TestTemplateResponse) => {
        // Kaydedilen template'i güncelle
        this.editableTestTemplate = response;
        this.testCard = response;
        // Comment yönetimi sayfasına geç
        if (response.id) {
          this.loadTemplateComments(response.id);
          this.showCommentManagement = true;
        }
      },
      error: (err: any) => {
        console.error('Error creating test template for comment management', err);
        this.showToast = true;
        this.toastType = 'error';
        this.toastMessage = 'Test kaydedilirken bir hata oluştu. Lütfen tekrar deneyin.';
      },
    });
  }

  loadTemplateComments(testTemplateId: number) {
    this.commentAdminService.getCommentsByTestTemplate({ testTemplateId }).subscribe({
      next: (comments: AdminTestCommentResponse[]) => {
        this.templateComments = comments || [];
      },
      error: (err: any) => {
        console.error('Error loading template comments', err);
      },
    });
  }

  addTemplateComment() {
    const newComment: AdminTestCommentRequest = {
      score: 1,
      title: '',
      text: ''
    };
    this.templateComments.push(newComment as AdminTestCommentResponse);
  }

  removeTemplateComment(index: number) {
    this.templateComments.splice(index, 1);
  }

  saveTemplateComments() {
    if (!this.editableTestTemplate?.id) {
      this.showToast = true;
      this.toastType = 'error';
      this.toastMessage = 'Test template ID is missing';
      return;
    }

    const commentRequests: AdminTestCommentRequest[] = this.templateComments.map(comment => ({
      commentId: comment.commentId,
      score: comment.score,
      title: comment.title,
      text: comment.text
    }));

    this.commentAdminService.updateTestTemplateComments({
      testTemplateId: this.editableTestTemplate.id,
      body: commentRequests
    }).subscribe({
      next: (updatedComments) => {
        this.templateComments = updatedComments;
        this.showCommentManagement = false;
        this.showToast = true;
        this.toastType = 'success';
        this.toastMessage = 'Yorumlar başarıyla güncellendi!';
      },
      error: (error) => {
        console.error('Error updating comments:', error);
        this.showToast = true;
        this.toastType = 'error';
        this.toastMessage = 'Yorumlar güncellenirken bir hata oluştu. Lütfen tekrar deneyin.';
      }
    });
  }

  saveNewTestWithQuestions() {
    // Update the scoring strategy in the update request
    if (this.selectedStrategy) {
      this.updateTestTemplate.scoringStrategy = this.selectedStrategy as 'SIMPLE_LINEAR' | 'WEIGHTED' | 'PERCENTAGE';
    }

    // Create new template first
    this.testTemplateAdminService.createTestTemplate({
      body: this.updateTestTemplate
    }).subscribe({
      next: (response: TestTemplateResponse) => {
        this.editableTestTemplate = response;
        this.testCard = response;
        
        // Update question template IDs
        this.templateQuestions.forEach(q => {
          q.testTemplateId = response.id;
        });
        
        // Save questions if there are any
        if (this.templateQuestions && this.templateQuestions.length > 0) {
          this.testTemplateAdminService.updateTestTemplateQuestions({
            id: response.id!,
            body: this.templateQuestions
          }).subscribe({
            next: (updatedQuestions) => {
              this.templateQuestions = updatedQuestions;
              // Finally upload image if selected
              if (this.selectedFile) {
                this.uploadImageForNewTemplate(response.id!);
              } else {
                this.showSuccessAndClose(true);
              }
            },
            error: (error) => {
              console.error('Error updating questions:', error);
              this.showToast = true;
              this.toastType = 'error';
              this.toastMessage = 'Sorular kaydedilirken bir hata oluştu.';
            }
          });
        } else {
          // No questions to save, just upload image if selected
          if (this.selectedFile) {
            this.uploadImageForNewTemplate(response.id!);
          } else {
            this.showSuccessAndClose(true);
          }
        }
      },
      error: (error) => {
        console.error('Error creating test template:', error);
        this.showToast = true;
        this.toastType = 'error';
        this.toastMessage = 'Test oluşturulurken bir hata oluştu.';
      }
    });
  }

  saveAllTestChanges() {
    if (!this.editableTestTemplate?.id) {
      // Yeni test için önce template'i oluştur
      this.saveNewTestWithQuestions();
      return;
    }

    // Update the scoring strategy in the update request
    if (this.selectedStrategy) {
      this.updateTestTemplate.scoringStrategy = this.selectedStrategy as 'SIMPLE_LINEAR' | 'WEIGHTED' | 'PERCENTAGE';
    }

    // Handle imageUrl based on whether a new image is selected
    if (!this.selectedFile && this.editableTestTemplate?.imagePath) {
      this.updateTestTemplate.imageUrl = this.editableTestTemplate.imagePath;
    } else if (this.selectedFile) {
      delete this.updateTestTemplate.imageUrl;
    }

    // First update the template info
    this.testTemplateAdminService.updateTestTemplate({
      id: this.editableTestTemplate.id,
      body: this.updateTestTemplate
    }).subscribe({
      next: (response: TestTemplateResponse) => {
        // Then update questions if there are any
        if (this.templateQuestions && this.templateQuestions.length > 0) {
          this.testTemplateAdminService.updateTestTemplateQuestions({
            id: this.editableTestTemplate!.id!,
            body: this.templateQuestions
          }).subscribe({
            next: (updatedQuestions) => {
              this.templateQuestions = updatedQuestions;
              // Finally upload image if selected
              if (this.selectedFile) {
                this.uploadSelectedImage();
              } else {
                this.showSuccessAndClose(true);
              }
            },
            error: (error) => {
              console.error('Error updating questions:', error);
              this.showToast = true;
              this.toastType = 'error';
              this.toastMessage = 'Sorular güncellenirken bir hata oluştu.';
            }
          });
        } else {
          // No questions to update, just upload image if selected
          if (this.selectedFile) {
            this.uploadSelectedImage();
          } else {
            this.showSuccessAndClose();
          }
        }
      },
      error: (error) => {
        console.error('Error updating test template:', error);
        this.showToast = true;
        this.toastType = 'error';
        this.toastMessage = 'Test güncellenirken bir hata oluştu.';
      }
    });
  }

  private showSuccessAndClose(isNewTest: boolean = false) {
    this.showToast = true;
    this.toastType = 'success';
    this.toastMessage = isNewTest ? 'Test başarıyla oluşturuldu!' : 'Test başarıyla güncellendi!';
    if (!isNewTest) {
      console.log('close modal');
      setTimeout(() => {
        this.closeModalEvent.emit();
      }, 2000);
    } 
  }

  closeToast() {
    this.showToast = false;
    this.toastMessage = '';
  }

  loadStrategies() {
    this.scoringAdminService.getAllScoringStrategies().subscribe({
      next: (strategies: ScoringStrategyResponse[]) => {
        this.availableStrategies = strategies;
      },
      error: (err: any) => {
        console.error('Error loading strategies', err);
      },
    });
  }


  uploadImage() {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = (e) => this.onFileSelected(e);
    fileInput.click();
  }

  async onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imageUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  uploadSelectedImage() {
    if (!this.selectedFile || !this.editableTestTemplate?.id) {
      console.error('No file selected or template ID missing');
      return;
    }

    this.testTemplateAdminService.uploadImage({
      testTemplateId: this.editableTestTemplate.id,
      body: {file: this.selectedFile}
    }).subscribe({
      next: (response) => {
        console.log('Image uploaded successfully', response);
        this.showSuccessAndClose();
      },
      error: (error) => {
        console.error('Error uploading image:', error);
        this.showToast = true;
        this.toastType = 'error';
        this.toastMessage = 'Görsel yüklenirken bir hata oluştu. Lütfen tekrar deneyin.';
      }
    });
  }

  uploadImageForNewTemplate(templateId: number) {
    if (!this.selectedFile) {
      console.error('No file selected');
      return;
    }

    this.testTemplateAdminService.uploadImage({
      testTemplateId: templateId,
      body: {file: this.selectedFile}
    }).subscribe({
      next: (response) => {
        console.log('Image uploaded successfully', response);
        this.showSuccessAndClose(true);
      },
      error: (error) => {
        console.error('Error uploading image:', error);
        this.showToast = true;
        this.toastType = 'error';
        this.toastMessage = 'Görsel yüklenirken bir hata oluştu. Lütfen tekrar deneyin.';
        this.closeModalEvent.emit();
      }
    });
  }

  getResult() {
    console.log('Test completion not implemented for TestTemplate workflow');
  }
}
