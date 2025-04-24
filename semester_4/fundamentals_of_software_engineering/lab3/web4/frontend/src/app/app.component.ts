import { Component, OnInit } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { ApiService } from './api.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, HeaderComponent, FormsModule, ReactiveFormsModule, HttpClientModule],
    templateUrl: './app.component.html',
    styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
    constructor(private router: Router, private apiService: ApiService) {}
    
    ngOnInit(): void {
        if (!this.apiService.getUsername()) this.router.navigate(['/login']);
        else this.router.navigate(['/']);
    }
}