import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { phl } from '@angular-extensions/pretty-html-log';
import { of } from 'rxjs';

const fakeSession: Partial<Session> = {
    name: "fake session",
    description: "description fake session",
    date: new Date("2024-12-04"),
    teacher_id: 1,
}

describe('FormComponent', () => {
    let component: FormComponent;
    let fixture: ComponentFixture<FormComponent>;
    let mockedSessionService: any
    let mockedRouter: any
    let mockActivatedRoute: any
    let mockedMatSnackBar: any
    let mockedSessionApiService: any

    beforeEach(async () => {
        mockedSessionService = {
            sessionInformation: {
                admin: true
            }
        }
        mockedRouter = {
            navigate: jest.fn(),
            url: '/sessions'
        }

        mockActivatedRoute = {
            snapshot: {
                paramMap: {
                    get: jest.fn()
                }
            }
        }

        mockedMatSnackBar = {
            open: jest.fn()
        };

        mockedSessionApiService = {
            create: jest.fn().mockReturnValue(of(null)),
            detail: jest.fn().mockReturnValue(of(fakeSession)),
            update: jest.fn().mockReturnValue(of(null))
        }

        await TestBed.configureTestingModule({

            imports: [
                RouterTestingModule,
                HttpClientModule,
                MatCardModule,
                MatIconModule,
                MatFormFieldModule,
                MatInputModule,
                ReactiveFormsModule,
                MatSnackBarModule,
                MatSelectModule,
                BrowserAnimationsModule
            ],
            providers: [
                { provide: Router, useValue: mockedRouter },
                { provide: ActivatedRoute, useValue: mockActivatedRoute },
                { provide: SessionService, useValue: mockedSessionService },
                { provide: MatSnackBar, useValue: mockedMatSnackBar },
                { provide: SessionApiService, useValue: mockedSessionApiService },
            ],
            declarations: [FormComponent]
        })
            .compileComponents();

        fixture = TestBed.createComponent(FormComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();

    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });


    it('Should create a new session', () => {
        component.sessionForm?.setValue(fakeSession)
        component.submit()

        expect(mockedSessionApiService.create).toHaveBeenCalledTimes(1)
        expect(mockedRouter.navigate).toHaveBeenCalledWith(['sessions'])

    })
    it('Should update a session', () => {
        mockedRouter.url = 'update'
        component.onUpdate = true
        component.ngOnInit()

        component.sessionForm?.setValue(fakeSession)
        component.submit()

        expect(mockedSessionApiService.update).toHaveBeenCalledTimes(1)
        expect(mockedRouter.navigate).toHaveBeenCalledWith(['sessions'])

    })

});
