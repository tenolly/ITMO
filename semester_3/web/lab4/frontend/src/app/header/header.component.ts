import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { ApiService } from '../api.service';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-header',
    standalone: true,
    imports: [],
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
    name: string = "Неавторизован :(";
    private routerSubscription: Subscription;

    constructor(private router: Router, private apiService: ApiService) {
        this.routerSubscription = this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.getUsername();
            }
        });
    }
    
    ngOnInit() {
        this.getUsername();
    }
    
    ngOnDestroy() {
        if (this.routerSubscription) {
            this.routerSubscription.unsubscribe();
        }
    }

    getUsername() {
        this.apiService.getUsername().subscribe({
            next: (response) => {
                this.name = response;
                this.redirectToHomePage();
            },
            error: (error) => {
                this.name = "Неавторизован :(";
                console.error('Ошибка:', error);
            }
        });
    }

    redirectToHomePage() {
        this.router.navigate(['/home']);
    }
    
    logout() {
        this.apiService.logout().subscribe({
            next: (response) => {
                console.log(response);
                this.router.navigate(['/']);
            },
            error: (error) => {
                console.error('Ошибка logout:', error);
            }
        });
    }
}
