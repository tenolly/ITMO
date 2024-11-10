import { Component, OnInit } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { AuthService } from './auth.service';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, HeaderComponent],
    templateUrl: './app.component.html',
    styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
    title = 'Лабораторная работа №3';

    constructor(private router: Router, private authService: AuthService) {}
    
    ngOnInit(): void {
        if (!this.authService.isAuthenticated()) this.router.navigate(['/login']);
        else this.router.navigate(['/']);
    }
}