import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../api.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [ReactiveFormsModule],
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})
export class LoginComponent {
    loginForm: FormGroup;
    private router: Router = new Router();

    constructor(private fb: FormBuilder, private apiService: ApiService) {
        this.loginForm = this.fb.group({
            login: ['', [Validators.required, Validators.minLength(6)]],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    onLogin() {
        if (this.loginForm.valid) {
            this.apiService.login(
                this.loginForm.controls['login'].value, 
                this.loginForm.controls['password'].value
            ).subscribe({
                next: (response) => {
                    this.redirectToHomePage();
                },
                error: (error) => {
                  console.error('Registration failed:', error);
                  alert(error.error);
                }
            });
        }
    }

    redirectToRegisterForm() {
        this.router.navigate(['/register']);
    }

    redirectToHomePage() {
        this.router.navigate(['/home']);
    }
}