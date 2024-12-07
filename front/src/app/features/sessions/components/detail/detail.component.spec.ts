import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { By } from '@angular/platform-browser';

const fakeSession: Session = {
    name: "fake session",
    description: "description fake session",
    date: new Date("2024-12-04"),
    teacher_id: 1,
    users: [5, 6],
    createdAt: new Date("2024-12-01"),
    updatedAt: new Date("2024-12-02"),
}

describe('DetailComponent', () => {
    let component: DetailComponent;
    let fixture: ComponentFixture<DetailComponent>;
    let service: SessionService;

    let mockedSessionService: any
    let mockedSessionApiService: any
    let mockedTeacherService: any
    let mockedMatSnackBar: any
    let mockedRouter: any
    let mockedActivatedRoute: any


    beforeEach(async () => {
        mockedSessionService = {
            sessionInformation: {
                admin: true,
                id: 1
            }
        }

        mockedSessionApiService = {
            detail: jest.fn().mockReturnValue(of(fakeSession)),
            delete: jest.fn(),
            participate: jest.fn(),
            unParticipate: jest.fn()
        }

        mockedTeacherService = {
            detail: jest.fn()
        }
        mockedMatSnackBar = {
            open: jest.fn()
        }
        mockedRouter = {
            navigate: jest.fn()
        }
        mockedActivatedRoute = {
            snapshot: {
                paramMap: {
                    get: jest.fn()
                }
            }
        }

        await TestBed.configureTestingModule({
            imports: [
                RouterTestingModule,
                HttpClientModule,
                MatSnackBarModule,
                ReactiveFormsModule
            ],
            declarations: [DetailComponent],
            providers: [
                { provide: SessionService, useValue: mockedSessionService },
                { provide: SessionApiService, useValue: mockedSessionApiService },
                { provide: TeacherService, useValue: mockedTeacherService },
                { provide: MatSnackBar, useValue: mockedMatSnackBar },
                { provide: Router, useValue: mockedRouter },
                { provide: ActivatedRoute, useValue: mockedActivatedRoute },
            ],
        })
            .compileComponents();
        service = TestBed.inject(SessionService);
        fixture = TestBed.createComponent(DetailComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('Should display the session info and delete button', () => {

        const h1 = fixture.nativeElement.querySelector('h1') as HTMLElement

        const description = fixture.nativeElement.querySelector('.description') as HTMLElement

        const deleteBtn = fixture.debugElement.query(
            By.css('button[data-testid="deleteBtn"]')
        );

        expect(description.textContent).toContain(fakeSession.description)
        expect(h1.textContent?.toLocaleLowerCase()).toContain(fakeSession.name)
        expect(deleteBtn).toBeTruthy()

    })

    it('Should delete the session', () => {

        mockedSessionApiService.delete.mockReturnValue(of(null))
        component.delete()

        expect(mockedSessionApiService.delete).toHaveBeenCalled()
        expect(mockedMatSnackBar.open).toHaveBeenCalledWith("Session deleted !", "Close", { "duration": 3000 })
        expect(mockedRouter.navigate).toHaveBeenCalledWith(['sessions'])

    })

    it('Should navigate back', () => {
        const historySpy = jest.spyOn(window.history, 'back')
        component.back()
        expect(historySpy).toHaveBeenCalled()
    })
    it('Should be able to partcipate', () => {
        mockedSessionApiService.participate.mockReturnValue(of(null))
        component.participate()
        expect(mockedSessionApiService.participate).toHaveBeenCalledTimes(1)
    })
    it('Should to able to unparticipate', () => {
        mockedSessionApiService.unParticipate.mockReturnValue(of(null))
        component.unParticipate()

        expect(mockedSessionApiService.unParticipate).toHaveBeenCalledTimes(1)
    })
});

