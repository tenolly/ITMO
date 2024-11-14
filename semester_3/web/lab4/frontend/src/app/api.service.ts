import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    private apiUrl = 'http://localhost:41742/api';

    constructor(private http: HttpClient) { }

    check(x: number, y: number, r: number): Observable<any> {
        return this.http.get(`${this.apiUrl}/check?x=${x}&y=${y}&r=${r}`, { withCredentials: true, responseType: 'text' });
    }

    getUsername(): Observable<any> {
        return this.http.post(`${this.apiUrl}/auth/access`, {}, { withCredentials: true, responseType: 'text' });
    }

    register(username: string, password: string): Observable<any> {
        const user = { username, password };
        return this.http.post(`${this.apiUrl}/auth/register`, user, { responseType: 'text' });
    }

    login(username: string, password: string): Observable<any> {
        const user = { username, password };
        return this.http.post(`${this.apiUrl}/auth/login`, user, { withCredentials: true, responseType: 'text' });
    }

    logout(): Observable<any> {
        return this.http.post(`${this.apiUrl}/auth/logout`, {}, { withCredentials: true, responseType: 'text' });
    }

    refreshToken(): Observable<any> {
        return this.http.post(`${this.apiUrl}/auth/refresh`, {}, { withCredentials: true, responseType: 'text' });
    }
}
