import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { AppService } from 'src/app/app.service';
import { Router } from '@angular/router';

import { ValidationErrors } from '@angular/forms/forms';
import { FormGroup, FormControl, Validators, FormBuilder } from "@angular/forms";

import { MustMatch } from 'src/app/_helpers/must-match.validator';

@Component({
  selector: 'app-password-change',
  templateUrl: './password-change.component.html',
  styleUrls: ['./password-change.component.css']
})
export class PasswordChangeComponent implements OnInit {

	id;
	token;
	
	passwordUpdateForm: FormGroup;
	errors = [];
	
 	constructor(private service: AppService, private route: ActivatedRoute, private router: Router, private formBuilder: FormBuilder) { }

  	ngOnInit() {
  		this.route.queryParams.subscribe(params => {
			this.token = params.token,
			this.id = params.id
		});
  		
  		this.passwordUpdateForm = this.formBuilder.group({
  	  		password: new FormControl('', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0|1-9])(?=.*[@$!%*?&])[A-Za-z0|1-9@$!%*?&]{8,16}$')]),
  	  		matchingPassword: new FormControl('', [Validators.required])
  	  	}, {
  	  		validator: MustMatch('password', 'matchingPassword')
  	  	});
  	}
  	
  	get password() { return this.passwordUpdateForm.get('password'); }
    get matchingPassword() { return this.passwordUpdateForm.get('matchingPassword'); }
    
    passwordChange(){
    	this.getFormValidationErrors();
    	    	  
  	  	if(this.errors.length == 0){
  	  		this.service.passwordChange(this.id, this.token, this.passwordUpdateForm);
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
