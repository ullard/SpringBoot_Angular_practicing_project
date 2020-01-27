import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { HttpClient, HttpResponse, HttpHeaders, HttpRequest, HttpParams, HttpXsrfTokenExtractor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

import { Md5 } from 'ts-md5/dist/md5';
import { switchMap } from 'rxjs/operators';

export class User {
	constructor(
		public username: string,
		public name: string,
		public email: string,
		public phone: string
	){ }
	
	toString(): string {
	       return 'username: ' + this.username + ' name: ' + this.name + ' email: ' + this.email + ' phone: ' + this.phone;
   }
}

export class Post {
	constructor(
		public author: User,
	    public title: string,
	    public content: string,
	    public uploadDate: string, 
	    public lastChangeDate: string
	){ }
	
	toString(): string {
       return 'author: ' + this.author + ' title: ' + this.title + ' content: ' + this.content + ' uploadDate: ' + this.uploadDate + ' lastChangeDate: ' + this.lastChangeDate;
   }
}


@Injectable({
  providedIn: 'root'
})
export class AppService {
	
	prefix = "https";
			
	constructor(private router: Router, private http: HttpClient, private cookieService: CookieService, private tokenExtractor: HttpXsrfTokenExtractor){}

	checkCredentials(){
		if (!(this.cookieService.check('access_token'))){
			this.router.navigate(['/login']);
	    }
	}
	
	isAuthenticated(): boolean{
		if(!(this.cookieService.check('access_token'))){
			return false;
		}
		
		return true;
	}
	
	getXsrfToken(){
		const token = this.tokenExtractor.getToken();
		
		return token;
	}
	
	async obtainAccessToken(loginData){
		
		localStorage.setItem("username", loginData.username);
		
	    const params = new HttpParams()
	    		.set('username',loginData.username)
	  			.set('password',loginData.password)
	    		.set('grant_type','password')
	    		.set('client_id','fooClientIdPassword');

	    const headers = new HttpHeaders({'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 'Authorization': 'Basic ' + btoa("fooClientIdPassword:secret")});
	    
	    return await this.http.post( this.prefix + '://localhost:8081/oauth/token', params.toString(), {headers: headers, withCredentials: true}).toPromise().then(
	    		data => { if(data.hasOwnProperty('error') && data["error"] == null){this.router.navigate(['/login/tfa']);} else{this.saveToken(data);}},
	    		err => {alert('Something went wrong.\n' + err.error["errors"]); this.router.navigate(['/login']); localStorage.removeItem('username');});
	    
	}
	
	async saveToken(token){
		  
	    var expireDate = new Date().getTime() + (1000 * token.expires_in);
	    
	    await this.cookieService.set("access_token", token.access_token, expireDate);
	    
	    window.location.href = "/";
	}

	async logout(){
		var expireDate = new Date();
		
		console.log(expireDate);
		
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 'Authorization': 'Bearer '+ this.cookieService.get('access_token')} );

		await this.http.post(this.prefix + '://localhost:8081/oauth/token/revokeById/' + this.cookieService.get('access_token'), {headers: headers, withCredentials: true}).toPromise().then(
				data => {console.log(data); this.cookieService.delete('access_token'); localStorage.clear(); window.location.href = "/login";},  			
		  		err => alert('Error.\n' + err.error["errors"])
		);
	}
	
	async registration(formData){
		
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'} );
		
		const params = new HttpParams()
	    		.set('firstname',formData.controls['firstName'].value.toString())
	  			.set('lastname',formData.controls['lastName'].value.toString())
	    		.set('username',formData.controls['username'].value.toString())
	    		.set('password',formData.controls['password'].value.toString())
	    		.set('matchingPassword',formData.controls['matchingPassword'].value.toString())
	    		.set('email',formData.controls['email'].value.toString())
	    		.set('phone',formData.controls['phone'].value.toString());
		
		await this.http.post(this.prefix + '://localhost:8082/user/registration', params.toString(), {headers: headers}).toPromise().then(
	  			data => {alert('Successful registration.\n' + data["message"]);},  			
	  			err => {alert('Failed registration.\n' + err.error["errors"]);}
	    	);
		
		this.router.navigate(['/']);
	}
	
	async registrationConfirm(token){
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'} );
		
		const params = new HttpParams().set('token', token);
				
		await this.http.post(this.prefix + '://localhost:8082/user/registrationConfirm', params.toString(), {headers: headers}).toPromise().then(
				data => alert('Registration confirmed.\n' + data["message"]),  			
		  		err => alert('Failed registration confirm.\n' + err.error["errors"])
	    	);
		
		this.router.navigate(['/']);
	}
	
	async passwordReset(email){
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'} );
		
		const params = new HttpParams().set('email', email);
		
		await this.http.post(this.prefix + '://localhost:8082/user/resetPassword', params.toString(), {headers: headers}).toPromise().then(
	  			data => { alert('Password has been reset.\n' + data["message"]); console.log(data); },  			
	  			err => {alert('Error during reset.\n' + err.error["errors"]);}
	  			);
		
		this.router.navigate(['/']);
	}
	
	async passwordChange(id, token, formData){
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'} );
		
		const params = new HttpParams()
				.set('id', id)
				.set('token', token)
				.set('password',formData.controls['password'].value.toString())
	    		.set('matchingPassword',formData.controls['matchingPassword'].value.toString());
    	
    	await this.http.post(this.prefix + '://localhost:8082/user/changePassword', params.toString(), {headers: headers}).toPromise().then(
	    		data => {alert('Successful password change.\n' + data["message"]); this.router.navigate(['/']);},
	    		err => {alert('Failed password change.\n' + err.error["errors"]);}
	    		);
	}
	
	async passwordUpdate(formData){
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 'Authorization': 'Bearer '+ this.cookieService.get('access_token'), 'X-XSRF-TOKEN': this.getXsrfToken()} );

		const params = new HttpParams()
				.set('oldPassword', formData.controls['oldPassword'].value.toString())
	    		.set('newPassword',formData.controls['newPassword'].value.toString());
		
		await this.http.post(this.prefix + '://localhost:8082/user/modifyPassword', params.toString(), {headers: headers, withCredentials: true}).toPromise().then(
	    		data => alert('Successful password change.\n' + data["message"]),
	    		err => alert('Invalid Request.\n' + err.error["errors"])
	    		);

	}
	
	async getUserData(username): Promise<User>{		
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'} );
		
		let data = await this.http.get<User>(this.prefix + '://localhost:8082/user/username/' + username, {headers: headers}).toPromise();
		
		return data;
	}
	
	async getPosts(username): Promise<Post[]>{
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'} );
		
		let data = await this.http.get<Post[]>(this.prefix + '://localhost:8082/post/posts/' + username, {headers: headers}).toPromise();
		
		return data;		
	}
	
	async createPost(postData){
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 'Authorization': 'Bearer '+ this.cookieService.get('access_token'), 'X-XSRF-TOKEN': this.getXsrfToken()} );
		
		const params = new HttpParams()
				.set('title', postData.title)
				.set('content', postData.content)
				.set('targetUsername', postData.targetUsername);
		
		let data = await this.http.post(this.prefix + '://localhost:8082/post/createPost', params.toString(), {headers: headers, withCredentials: true}).toPromise();
		
		return data;
	}
	
	async getTfa(){
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 'Authorization': 'Bearer '+ this.cookieService.get('access_token')} );
		
		let data = await this.http.get(this.prefix + '://localhost:8082/user/tfa', {headers: headers, withCredentials: true, responseType: 'text'}).toPromise();
	
		return data;
	}
	
	async changeTfa(usingTfa: boolean){
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 'Authorization': 'Bearer '+ this.cookieService.get('access_token'), 'X-XSRF-TOKEN': this.getXsrfToken()} );
		
		const params = new HttpParams().set('usingTfa', usingTfa.toString());
				
		let data = await this.http.post(this.prefix + '://localhost:8082/user/tfa', params.toString(), {headers: headers, withCredentials: true}).toPromise();
		
		return data;
	}
	
	async loginWithTfa(verificationCode){
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 'Authorization': 'Basic ' + btoa("fooClientIdPassword:secret")} );
		
		const params = new HttpParams().set('verificationCode', verificationCode);
		
		await this.http.post(this.prefix + '://localhost:8081/secure/two_factor_authentication/verification', params.toString(), {headers: headers, withCredentials: true}).toPromise().then(
	    		data => { this.secondRound(); },
	    		err => {alert('Something went wrong.\n' + err.error["message"]); this.router.navigate(['/login']); localStorage.removeItem('username');});
	}
	
	async secondRound(){
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8', 'Authorization': 'Basic ' + btoa("fooClientIdPassword:secret")} );
				
		await this.http.post(this.prefix + '://localhost:8081/oauth/token', '', {headers: headers, withCredentials: true}).toPromise().then(
	    		data => { this.saveToken(data); },
	    		err => alert('Invalid Request.\n' + err.error["errors"]));
	}
	
	async getUsers(searchValue): Promise<User[]>{
		const headers = new HttpHeaders( {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'} );
				
		let data = await this.http.get<User[]>(this.prefix + '://localhost:8082/user/search/' + searchValue, {headers: headers}).toPromise();
		
		return data;		
	}

}
