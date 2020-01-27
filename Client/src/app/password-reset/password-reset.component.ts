import { Component } from '@angular/core';
import { AppService } from 'src/app/app.service';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent{

	email;
	
	constructor(private service: AppService) {}

	passwordReset(){
		this.service.passwordReset(this.email);
	}

}
