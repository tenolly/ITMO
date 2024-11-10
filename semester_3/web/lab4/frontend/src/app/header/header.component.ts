import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-header',
    standalone: true,
    imports: [],
    templateUrl: './header.component.html',
    styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
    name: String = "Неавторизован :(";

    constructor(private router: Router, private authService: AuthService) {}
    
    ngOnInit(): void {
        if (this.authService.isAuthenticated()) this.name = this.authService.username;
    }
    
}
