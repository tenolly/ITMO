import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../api.service';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [ReactiveFormsModule],
    templateUrl: './register.component.html',
    styleUrl: './register.component.css'
})
export class RegisterComponent {
    registerForm: FormGroup;
    private router: Router = new Router();

    constructor(private fb: FormBuilder, private apiService: ApiService) {
        this.registerForm = this.fb.group({
            login: ['', [Validators.required, Validators.minLength(6)]],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    onRegister() {
        if (this.registerForm.valid) {
            this.apiService.register(
                this.registerForm.controls['login'].value, 
                this.registerForm.controls['password'].value
            ).subscribe({
                next: (response) => {
                    this.redirectToHomeForm();
                },
                error: (error) => {
                  console.error('Registration failed:', error);
                  alert(error.error);
                }
            });
        }
    }

    redirectToHomeForm() {
        this.router.navigate(['/login']);
    }
}
