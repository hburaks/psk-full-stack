import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-generic-card-detail',
  templateUrl: './generic-card-detail.component.html',
  styleUrls: ['./generic-card-detail.component.scss']
})
export class GenericCardDetailComponent {
  @Input() text: string = '';
  @Input() title: string = '';
  @Input() cover: string[] = [];
  @Input() isTextCenter: boolean = false;
}
