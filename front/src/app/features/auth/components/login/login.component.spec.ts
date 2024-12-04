import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { phl } from '@angular-extensions/pretty-html-log';

const validLoginFormData: LoginRequest = {
    email: "user@mail.com",
    password: "password"
}
const invalidLoginFormData: LoginRequest = {
    email: "",
    password: ""
}

const responseForValidCredentials = {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MzMyMzgzNDgsImV4cCI6MTczMzMyNDc0OH0.UPFvmM09njhH6AJ0zGA1-IWlYR2qOsxaQyrGVTIDPyyzoN99bCD1gTtofLOD_fmk11lf4h-Grwht4JkZTJ42HA",
    "type": "Bearer",
    "id": 1,
    "username": "yoga@studio.com",
    "firstName": "Admin",
    "lastName": "Admin",
    "admin": true
}


describe('LoginComponent', () => {
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let mockedAuthService: any
    let mockedRouter: any
    let mockedSessionService: any


    beforeEach(async () => {

        mockedAuthService = {
            login: jest.fn().mockReturnValue({ subscribe: jest.fn() })
        }
        mockedRouter = {
            navigate: jest.fn()
        }
        mockedSessionService = {
            logIn: jest.fn()
        }
        await TestBed.configureTestingModule({
            declarations: [LoginComponent],
            providers: [
                { provide: AuthService, useValue: mockedAuthService },
                { provide: Router, useValue: mockedRouter },
                { provide: SessionService, useValue: mockedSessionService },
            ],
            imports: [
                RouterTestingModule,
                BrowserAnimationsModule,
                HttpClientModule,
                MatCardModule,
                MatIconModule,
                MatFormFieldModule,
                MatInputModule,
                ReactiveFormsModule]
        })
            .compileComponents();
        fixture = TestBed.createComponent(LoginComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('Should login the user when valid credentials are sent', () => {

        mockedAuthService.login.mockReturnValue(of(responseForValidCredentials))
        component.form.setValue(validLoginFormData)
        component.submit()

        expect(mockedAuthService.login).toHaveBeenCalledWith(validLoginFormData)
        expect(mockedSessionService.logIn).toHaveBeenCalledWith(responseForValidCredentials)
        expect(mockedRouter.navigate).toHaveBeenCalledWith(["/sessions"])
    })
    it('Should not login the user when invalid credentials are sent', () => {
        const errorMsg = "An error occurred"
        mockedAuthService.login.mockReturnValue(throwError(() => new Error(errorMsg)))
        component.form.setValue(invalidLoginFormData)
        component.submit()

        expect(mockedSessionService.logIn).not.toHaveBeenCalled()
        expect(mockedRouter.navigate).not.toHaveBeenCalledWith(["/sessions"])
        expect(errorMsg).toBeDefined()
    })
});
