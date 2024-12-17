import { Injectable } from '@angular/core';
import { UserResponse } from 'src/app/services/models';
import { UserService } from 'src/app/services/services';

@Injectable({
  providedIn: 'root'
})
export class CustomUserService {

  user: UserResponse | null = null;

  constructor(private userService: UserService) { }

  getUser() {
    this.userService.getUser().subscribe(
      {next:(user) => {
        this.user = user;
        return user;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
}
