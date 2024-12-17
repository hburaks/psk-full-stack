import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-empty-card',
  templateUrl: './empty-card.component.html',
  styleUrls: ['./empty-card.component.scss']
})
export class EmptyCardComponent {

  @Input() isBlogEditable: boolean = false;
  
  @Output() addBlogEvent = new EventEmitter<void>();



  ngOnInit(): void {
  }

  addBlog() {
    this.addBlogEvent.emit();
  }

}
