import { Component } from '@angular/core';
import { Validators } from '@angular/forms';
import { UserRequest, UserResponse } from 'src/app/services/models';
import { UserService } from 'src/app/services/services';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss'],
})
export class EditUserComponent {
  user: UserResponse | null = null;

  firstName: string = '';
  lastName: string = '';
  phoneNumber: string = '';
  birthYear: string = '1990';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.fetchUser();
  }

  fetchUser() {
    this.userService.getUser().subscribe((user) => {
      this.user = user;
      this.firstName = user.firstname || '';
      this.lastName = user.lastname || '';
      this.phoneNumber = user.phoneNumber || '';
      this.birthYear = user.birthYear || '1990';
    });
  }

  updateName() {
    const userRequest: UserRequest = {
      firstname: this.firstName,
      lastname: this.lastName,
    };
    this.userService
      .updateUser({
        body: userRequest,
      })
      .subscribe((user) => {
        this.user = user;
      });
  }

  updatePhoneNumber() {
    const userRequest: UserRequest = {
      phoneNumber: this.phoneNumber,
    };
    this.userService
      .updateUser({
        body: userRequest,
      })
      .subscribe((user) => {
        this.user = user;
      });
  }

  updateBirthYear() {
    const userRequest: UserRequest = {
      birthYear: this.birthYear.toString(),
    };
    this.userService
      .updateUser({
        body: userRequest,
      })
      .subscribe((user) => {
        this.user = user;
      });
  }
}
