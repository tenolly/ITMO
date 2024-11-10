import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

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

    constructor(private fb: FormBuilder) {
        this.loginForm = this.fb.group({
            login: ['', [Validators.required, Validators.minLength(6)]],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    onLogin() {
        if (this.loginForm.valid) {
            const formData = this.loginForm.value;
            console.log('Вход выполнен успешно:', formData);
        }
    }

    redirectToRegisterForm() {
        this.router.navigate(['/register']);
    }
}