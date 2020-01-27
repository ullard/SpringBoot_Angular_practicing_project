import { ValidationErrors } from '@angular/forms/forms';
import { Component, OnInit } from '@angular/core';
import { AppService } from 'src/app/app.service';
import { FormGroup, FormControl, Validators, FormBuilder } from "@angular/forms";


import { MustMatch } from 'src/app/_helpers/must-match.validator';

@Component({
  selector: 'app-registration',
  providers: [AppService],
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit{

  registrationForm: FormGroup;
  
  errors = [];
  
  constructor(private service: AppService, private formBuilder: FormBuilder) {}
  
  /*reactive form validation*/
  ngOnInit() {
  	this.registrationForm = this.formBuilder.group({
  		firstName: new FormControl('', [Validators.required, Validators.minLength(1)]),
  		lastName: new FormControl('', [Validators.required, Validators.minLength(1)]),
  		username: new FormControl('', [Validators.required, Validators.pattern('[A-Za-z0|1-9]{3,50}')]),
  		password: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0|1-9])(?=.*[@$!%*?&])[A-Za-z0|1-9@$!%*?&]{8,16}$')]),
  		matchingPassword: new FormControl('', [Validators.required]),
  		email: new FormControl('', [Validators.required, Validators.pattern('^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$')]),
  		phone: new FormControl('', [Validators.required, Validators.pattern('^[1-9]{1}[0-9]{1,13}$')])
  	}, {
  		validator: MustMatch('password', 'matchingPassword')
  	});
  }
  
  /*get controls() { return this.registrationForm.controls; } */

  get firstName() { return this.registrationForm.get('firstName'); }
  get lastName() { return this.registrationForm.get('lastName'); }
  get username() { return this.registrationForm.get('username'); }
  get password() { return this.registrationForm.get('password'); }
  get matchingPassword() { return this.registrationForm.get('matchingPassword'); }
  get email() { return this.registrationForm.get('email'); }
  get phone() { return this.registrationForm.get('phone'); }
  
  
  registration(){

	  this.getFormValidationErrors();
	  	  
	  if(this.errors.length == 0)
	  {
		  this.service.registration(this.registrationForm);
	  }
	  
	  
  }
  
  getFormValidationErrors() {
	  
	  this.errors = [];
	  
	  Object.keys(this.registrationForm.controls).forEach(key => {

		  const controlErrors: ValidationErrors = this.registrationForm.get(key).errors;
	  
		  if (controlErrors != null) {
		        Object.keys(controlErrors).forEach(keyError => {
		        	this.errors.push(keyError);
	      		});
		        
	      }
	  });
	  
  }

}
