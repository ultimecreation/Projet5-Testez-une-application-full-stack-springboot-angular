
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

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
    },
    {
        id: 2,
        name: "session 2",
        description: "description session 2",
        date: new Date("2024-12-04"),
        teacher_id: 1,
        users: [3, 4],
        createdAt: new Date("2024-12-01"),
        updatedAt: new Date("2024-12-02"),
    },
]
const fakeSession: Session = {
    name: "fake session",
    description: "description fake session",
    date: new Date("2024-12-04"),
    teacher_id: 1,
    users: [5, 6],
    createdAt: new Date("2024-12-01"),
    updatedAt: new Date("2024-12-02"),
}
const firstSessionUpdated: Session = {
    name: "session 1 Updated",
    description: "description session 1 Updated",
    date: new Date("2024-12-04"),
    teacher_id: 1,
    users: [5, 6],
    createdAt: new Date("2024-12-01"),
    updatedAt: new Date("2024-12-02"),
}

describe('SessionsService', () => {
    let sessionApiService: SessionApiService;
    let httpController: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [SessionApiService]
        });
        sessionApiService = TestBed.inject(SessionApiService);
        httpController = TestBed.inject(HttpTestingController);


    });

    it('should be created', () => {
        expect(sessionApiService).toBeTruthy();
    });


    it('Should return the list of sessions', () => {

        sessionApiService.all().subscribe(data => {
            expect(data).toEqual(sessionsData)
        })

        const req = httpController.expectOne("api/session")
        req.flush({
            data: sessionsData
        })
    })

    it(`Should get one session by it's id`, () => {
        const id = sessionsData[0].id!.toString()
        sessionApiService.detail(id).subscribe(data => {
            expect(data).toBe(sessionsData[1])
        })

        const req = httpController.expectOne(`api/session/${id}`)
        req.flush({ data: sessionsData[1] })
        expect(req.request.method).toBe("GET")
    })

    it(`Should delete one session by it's id`, () => {
        const id = sessionsData[0].id!.toString()
        sessionApiService.delete(id).subscribe()

        const req = httpController.expectOne(`api/session/${id}`)
        expect(req.request.method).toBe("DELETE")
    })

    it(`Should create a new session`, () => {
        sessionApiService.create(fakeSession).subscribe(data => {
            expect(data).toEqual(fakeSession)
        })

        const req = httpController.expectOne(`api/session`)
        req.flush({
            data: fakeSession
        })

        expect(req.request.method).toBe("POST")
    })
    it(`Should update a session`, () => {
        const id = sessionsData[0].id!.toString()
        sessionApiService.update(id, firstSessionUpdated).subscribe(data => {
            expect(data).toEqual(firstSessionUpdated)
        })

        const req = httpController.expectOne(`api/session/${id}`)
        req.flush({
            data: firstSessionUpdated
        })

        expect(req.request.method).toBe("PUT")
    })

    it(`Should add a new user to a session`, () => {
        const id = sessionsData[0].id!.toString()
        const userId = "1"
        sessionApiService.participate(id, userId).subscribe(() => { })

        const req = httpController.expectOne(`api/session/${id}/participate/${userId}`)

        expect(req.request.method).toBe("POST")
    })
    it(`Should remove a user to a session`, () => {
        const id = sessionsData[0].id!.toString()
        const userId = "1"
        sessionApiService.unParticipate(id, userId).subscribe(() => { })

        const req = httpController.expectOne(`api/session/${id}/participate/${userId}`)

        expect(req.request.method).toBe("DELETE")
    })
});
