import { Component, OnInit } from '@angular/core';
import { AppService } from './../app.service';
import { FormGroup, FormControl, Validators, FormBuilder } from "@angular/forms";
import { MustMatch } from 'src/app/_helpers/must-match.validator';
import { ValidationErrors } from '@angular/forms/forms';

@Component({
  selector: 'app-settings-pass-change',
  templateUrl: './settings-pass-change.component.html',
  styleUrls: ['./settings-pass-change.component.css']
})
export class SettingsPassChangeComponent implements OnInit {

  constructor(private service: AppService, private formBuilder: FormBuilder){}
  
  passwordUpdateForm: FormGroup;
  
  errors = [];

  ngOnInit() {
	  this.service.checkCredentials();
	  
	  this.passwordUpdateForm = this.formBuilder.group({
		  	oldPassword: new FormControl('', [Validators.required]),
	  		newPassword: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0|1-9])(?=.*[@$!%*?&])[A-Za-z0|1-9@$!%*?&]{8,16}$')]),
	  		matchingPassword: new FormControl('', [Validators.required])
	  	}, {
	  		validator: MustMatch('newPassword', 'matchingPassword')
	  	});
  }
  
  get oldPassword() { return this.passwordUpdateForm.get('oldPassword'); }
  get newPassword() { return this.passwordUpdateForm.get('newPassword'); }
  get matchingPassword() { return this.passwordUpdateForm.get('matchingPassword'); }

  updatePass(){
	  this.getFormValidationErrors();
	  
	  if(this.errors.length == 0)
	  {  
		  this.service.passwordUpdate(this.passwordUpdateForm);
	  }
  }
  
  getFormValidationErrors() {	  
	  this.errors = [];
	  
	  Object.keys(this.passwordUpdateForm.controls).forEach(key => {
		  const controlErrors: ValidationErrors = this.passwordUpdateForm.get(key).errors;
	  
		  if (controlErrors != null) {
		        Object.keys(controlErrors).forEach(keyError => {
		        	this.errors.push(keyError);
	      		});
	      }
	  });
  }
  
}
