import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';
import { phl } from '@angular-extensions/pretty-html-log';
const sessionsData: Session[] = [
    {
        id: 1,
        name: "session 1",
        description: "description session 1",
        date: new Date("2024-12-04"),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date("2024-12-01"),
        updatedAt: new Date("2024-12-02"),
    }
]
describe('ListComponent', () => {
    let component: ListComponent;
    let fixture: ComponentFixture<ListComponent>;
    let mockedSessionApiService: any
    let mockedSessionService: any


    beforeEach(async () => {
        mockedSessionApiService = {
            all: jest.fn().mockReturnValue(of(sessionsData))
        };
        mockedSessionService = {
            sessionInformation: {
                admin: true
            }
        }

        await TestBed.configureTestingModule({
            declarations: [ListComponent],
            imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule,],
            providers: [
                { provide: SessionService, useValue: mockedSessionService },
                { provide: SessionApiService, useValue: mockedSessionApiService },
            ]
        })
            .compileComponents();

        fixture = TestBed.createComponent(ListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('Should display the list of session', () => {

        let response: Session[] = []
        component.sessions$.subscribe(session =>
            response = session
        );
        expect(sessionsData).toEqual(response);


        const createBtn = fixture.debugElement.query(
            By.css('button[data-testid="createBtn"]')
        )
        expect(createBtn).toBeTruthy()


        const detailBtn = fixture.debugElement.query(
            By.css('button[data-testid="detailBtn"]')
        )
        expect(detailBtn).toBeTruthy()
    })


});
