/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { LojaTestModule } from '../../../test.module';
import { CoresDetailComponent } from '../../../../../../main/webapp/app/entities/cores/cores-detail.component';
import { CoresService } from '../../../../../../main/webapp/app/entities/cores/cores.service';
import { Cores } from '../../../../../../main/webapp/app/entities/cores/cores.model';

describe('Component Tests', () => {

    describe('Cores Management Detail Component', () => {
        let comp: CoresDetailComponent;
        let fixture: ComponentFixture<CoresDetailComponent>;
        let service: CoresService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CoresDetailComponent],
                providers: [
                    CoresService
                ]
            })
            .overrideTemplate(CoresDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CoresDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CoresService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Cores(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.cores).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
