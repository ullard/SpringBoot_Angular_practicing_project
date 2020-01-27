import { Component, OnInit } from '@angular/core';
import { AppService, User, Post } from './../app.service';
import { ActivatedRoute } from "@angular/router";
import { Observable } from 'rxjs';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
	
	username = "";
	
	authenticated = this.service.isAuthenticated();
	
	user: User = new User('','','','');
	
	posts: Post[] = [];
	
	postData = {targetUsername: "", title: "", content: ""};
	
	constructor(private service: AppService, private route: ActivatedRoute){}
	 
    ngOnInit(){
    	        
        this.route.paramMap.subscribe(params => {
		    this.username = params.get("username")
		})
                
        this.getUser();
    }
    
    getUser(){
    	this.service.getUserData(this.username).then(data => {this.user = data; this.getPosts(); this.postData.targetUsername = this.username;}, err => alert('Error.\n' + err.error["errors"]));
    }
    
    getPosts(){
    	this.service.getPosts(this.username).then(data => {this.posts = data;}, err => alert('Error.\n' + err.error["errors"]));
    }
    
    createPost(){
    	    	
    	if(this.authenticated){
    		this.service.createPost(this.postData).then(data => window.location.reload(), err => alert('Error during post.\n' + err.error["errors"]));
    	}

    }
    
    
    
    

}
