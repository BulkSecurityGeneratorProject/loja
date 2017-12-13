/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { LojaTestModule } from '../../../test.module';
import { CoresComponent } from '../../../../../../main/webapp/app/entities/cores/cores.component';
import { CoresService } from '../../../../../../main/webapp/app/entities/cores/cores.service';
import { Cores } from '../../../../../../main/webapp/app/entities/cores/cores.model';

describe('Component Tests', () => {

    describe('Cores Management Component', () => {
        let comp: CoresComponent;
        let fixture: ComponentFixture<CoresComponent>;
        let service: CoresService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CoresComponent],
                providers: [
                    CoresService
                ]
            })
            .overrideTemplate(CoresComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CoresComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CoresService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Cores(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.cores[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
