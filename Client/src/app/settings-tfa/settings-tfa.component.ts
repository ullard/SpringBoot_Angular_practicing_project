import { Component, OnInit } from '@angular/core';
import { AppService } from './../app.service';

@Component({
  selector: 'app-settings-tfa',
  templateUrl: './settings-tfa.component.html',
  styleUrls: ['./settings-tfa.component.css']
})
export class SettingsTfaComponent implements OnInit {

  usingTfa: boolean = false;
  qr = "";
	
  constructor(private service: AppService){}

  ngOnInit() {
	  this.service.checkCredentials();
	  
	  if(this.service.isAuthenticated()){
	  	this.service.getTfa().then(data => {if(data != ""){this.usingTfa=true; this.qr = data;}else{this.usingTfa=false;}}, err => alert('Error.\n' + err.error["errors"]));
	  }
	  
  }

  changeTfa(){
	  if(this.usingTfa){
		  this.usingTfa = false;
		  		  
		  this.service.changeTfa(this.usingTfa).then(data => window.location.reload(), err => alert('Error.\n' + err.error["errors"]));
	  }
	  else if(!this.usingTfa){
		  this.usingTfa = true;
		  		  
		  this.service.changeTfa(this.usingTfa).then(data => window.location.reload(), err => alert('Error.\n' + err.error["errors"]));
	  }
  }
  
  check(){
	  if(this.qr != ""){
		  this.usingTfa = true;
	  }
  }
  
}
