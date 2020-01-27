import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { AppService } from 'src/app/app.service';

@Component({
  selector: 'app-registration-confirm',
  templateUrl: './registration-confirm.component.html',
  styleUrls: ['./registration-confirm.component.css']
})
export class RegistrationConfirmComponent implements OnInit {
	
  token;

  constructor(private service: AppService, private route: ActivatedRoute) {}

  ngOnInit() {
	  this.route.queryParams.subscribe(params =>
	  		this.token = params.token
	  );
			  	  
	  this.service.registrationConfirm(this.token);
	  
  }

}
