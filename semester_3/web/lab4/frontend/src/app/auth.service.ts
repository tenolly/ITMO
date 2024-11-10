import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private _username: string = '';

    constructor(private router: Router) { }

    login(username: string): void {
        this._username = username;
        localStorage.setItem('username', username);
        this.router.navigate(['/']);
    }

    logout(): void {
        this._username = '';
        localStorage.removeItem('username');
        this.router.navigate(['/login']);
    }

    isAuthenticated(): boolean {
        return this._username !== '';
    }

    get username(): string {
        return this._username;
    }

    set username(name: string) {
        this._username = name;
    }
}
