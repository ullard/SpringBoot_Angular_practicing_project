import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { AppService, User, Post } from 'src/app/app.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
		
	constructor(private service: AppService, private router: Router) {}
		
	authenticated = this.service.isAuthenticated();
	
	searchResult: User[] = [];
	
	nonEmpty: boolean = false;
	
	search: string;
	
	ngOnInit(){
    }
	
    logout() {
        this.service.logout();
    }
    
    profile(){
    	this.router.navigate(['/profile/' + localStorage.getItem("username")]); 
    }
    
    onSearchChange(searchValue: string): void {  
    	  console.log(searchValue);
    	  
    	  this.searchResult = [];
    	  console.log(this.searchResult);
    	  
    	  this.nonEmpty = false;
    	  console.log(this.nonEmpty);
   
    	  if(searchValue != ""){
        	  this.service.getUsers(searchValue).then(data => {console.log(data); this.searchResult = data; if(data.length > 0){this.nonEmpty=true;}else{this.nonEmpty=false;} console.log(this.searchResult); console.log(this.nonEmpty); }, err => alert('Error.\n' + err.error["errors"]));
    	  } 	  
    }
    
    searchNavigate(username){
		this.search = "";
    	
    	this.searchResult = [];
    	
    	this.nonEmpty = false;
    	
    	window.location.href = "/profile/" + username;
    }
	
}
