import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { phl } from '@angular-extensions/pretty-html-log';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';


const validRegisterFormData: RegisterRequest = {
    email: "user@mail.com",
    firstName: "firstname",
    lastName: "lastname",
    password: "password"
}
const invalidRegisterFormData: RegisterRequest = {
    email: "",
    firstName: "",
    lastName: "",
    password: ""
}



describe('RegisterComponent', () => {
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;
    let mockedAuthService: any
    let mockedRouter: any


    beforeEach(async () => {
        mockedAuthService = {
            register: jest.fn().mockReturnValue({ subscribe: jest.fn })
        }
        mockedRouter = {
            navigate: jest.fn()
        }

        await TestBed.configureTestingModule({
            declarations: [RegisterComponent],
            providers: [
                { provide: AuthService, useValue: mockedAuthService },
                { provide: Router, useValue: mockedRouter },
            ],
            imports: [
                BrowserAnimationsModule,
                HttpClientModule,
                ReactiveFormsModule,
                MatCardModule,
                MatFormFieldModule,
                MatIconModule,
                MatInputModule
            ]
        })
            .compileComponents();

        fixture = TestBed.createComponent(RegisterComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });


    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('Should register a new user when all fields are valid', () => {

        mockedAuthService.register.mockReturnValue(of(validRegisterFormData))
        component.form.setValue(validRegisterFormData);
        component.submit();

        expect(mockedAuthService.register).toHaveBeenCalledWith(validRegisterFormData);
        expect(mockedRouter.navigate).toHaveBeenCalledWith(["/login"])

    })
    it('Should not register a new user when some or all fields are empty', () => {

        const errorMsg = "An error occurred"
        mockedAuthService.register.mockReturnValue(throwError(() => new Error(errorMsg)))
        component.form.setValue(invalidRegisterFormData);
        component.submit();

        expect(mockedRouter.navigate).not.toHaveBeenCalledWith(["/login"])
        expect(errorMsg).toBeDefined();


    })
});
