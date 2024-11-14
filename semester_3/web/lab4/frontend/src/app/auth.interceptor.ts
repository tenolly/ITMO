import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    private isRefreshing = false;

    constructor(private apiService: ApiService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            catchError((error: HttpErrorResponse) => {
                console.log(error.status, !this.isRefreshing)
                if (error.status === 401 && !this.isRefreshing) {
                    this.isRefreshing = true;

                    return this.apiService.refreshToken().pipe(
                        switchMap(() => {
                            this.isRefreshing = false;
                            return next.handle(req);
                        }),
                        catchError(refreshError => {
                            this.isRefreshing = false;
                            this.apiService.logout();
                            return throwError(() => new Error(refreshError));
                        })
                    );
                }

                return throwError(() => new Error(error.message));
            })
        );
    }
}
