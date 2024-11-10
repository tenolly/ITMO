import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

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

    constructor(private fb: FormBuilder) {
        this.registerForm = this.fb.group({
            login: ['', [Validators.required, Validators.minLength(6)]],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    onRegister() {
        if (this.registerForm.valid) {
            const formData = this.registerForm.value;
            console.log('Регистрация выполнена успешна:', formData);
        }
    }

    redirectToHomeForm() {
        this.router.navigate(['/login']);
    }
}
