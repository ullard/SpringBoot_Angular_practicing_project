import { AppService } from './../app.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'home-header',
  providers: [AppService],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

	constructor(private service: AppService){}
	 
    ngOnInit(){
        this.service.checkCredentials();
    }

}
