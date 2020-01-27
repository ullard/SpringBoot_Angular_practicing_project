import { Component, OnInit } from '@angular/core';
import { AppService } from './../app.service';

@Component({
  selector: 'app-tfa',
  templateUrl: './tfa.component.html',
  styleUrls: ['./tfa.component.css']
})
export class TfaComponent implements OnInit{

  verificationCode;
	
  constructor(private service: AppService) { }

  ngOnInit() {
  }

  submit() {
      this.service.loginWithTfa(this.verificationCode);
  }
  
}
