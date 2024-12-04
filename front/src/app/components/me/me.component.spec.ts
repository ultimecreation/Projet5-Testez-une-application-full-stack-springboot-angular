import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { phl } from '@angular-extensions/pretty-html-log';
import { User } from 'src/app/interfaces/user.interface';

describe('MeComponent', () => {
    let component: MeComponent;
    let fixture: ComponentFixture<MeComponent>;
    let mockedRouter: any
    let mockedUserService: any
    let mockedSessionService: any
    let mockedMatSnackBar: any

    const fakeUser: User = {
        id: 1,
        email: "user@mail.com",
        lastName: "lastname",
        firstName: "firstname",
        admin: false,
        password: "123456",
        createdAt: new Date()
    }

    beforeEach(async () => {
        mockedUserService = {
            delete: jest.fn(),
            getById: jest.fn().mockReturnValue(of(fakeUser))

        }
        mockedRouter = {
            navigate: jest.fn()
        }
        mockedSessionService = {
            sessionInformation: {
                admin: true,
                id: 1
            },
            logOut: jest.fn()
        }
        mockedMatSnackBar = {
            open: jest.fn()
        };
        await TestBed.configureTestingModule({
            declarations: [MeComponent],
            imports: [
                MatSnackBarModule,
                HttpClientModule,
                MatCardModule,
                MatFormFieldModule,
                MatIconModule,
                MatInputModule
            ],
            providers: [
                { provide: SessionService, useValue: mockedSessionService },
                { provide: UserService, useValue: mockedUserService },
                { provide: Router, useValue: mockedRouter },
                { provide: MatSnackBar, useValue: mockedMatSnackBar },
            ],
        })
            .compileComponents();

        fixture = TestBed.createComponent(MeComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('Should displays the user informations', () => {
        component.ngOnInit()
        component.user = fakeUser
        expect(mockedUserService.getById).toHaveBeenCalledWith('1')

        const h1 = fixture.nativeElement.querySelector('h1') as HTMLElement
        const fullname = fixture.nativeElement.querySelector('.mat-card-content p:nth-of-type(1)') as HTMLElement
        const email = fixture.nativeElement.querySelector('.mat-card-content p:nth-of-type(2)') as HTMLElement

        expect(h1.textContent).toBe("User information")
        expect(fullname.textContent).toContain(`${fakeUser.firstName} ${fakeUser.lastName.toLocaleUpperCase()}`)
        expect(email.textContent).toContain(fakeUser.email)

        if (fakeUser.admin === true) {
            const adminStatus = fixture.nativeElement.querySelector('.mat-card-content p:nth-of-type(3)') as HTMLElement
            expect(adminStatus.textContent).toContain("You are admin")
        }
    })

    it('Should logout the user', () => {
        const id = mockedSessionService.sessionInformation.id.toString()
        mockedUserService.delete.mockReturnValue(of(id))
        component.delete()

        expect(mockedUserService.delete).toHaveBeenCalledWith('1')
        expect(mockedSessionService.logOut).toHaveBeenCalled()
        expect(mockedRouter.navigate).toHaveBeenCalledWith(["/"])
    })
});
