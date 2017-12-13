/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { LojaTestModule } from '../../../test.module';
import { LocalidadesDetailComponent } from '../../../../../../main/webapp/app/entities/localidades/localidades-detail.component';
import { LocalidadesService } from '../../../../../../main/webapp/app/entities/localidades/localidades.service';
import { Localidades } from '../../../../../../main/webapp/app/entities/localidades/localidades.model';

describe('Component Tests', () => {

    describe('Localidades Management Detail Component', () => {
        let comp: LocalidadesDetailComponent;
        let fixture: ComponentFixture<LocalidadesDetailComponent>;
        let service: LocalidadesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [LocalidadesDetailComponent],
                providers: [
                    LocalidadesService
                ]
            })
            .overrideTemplate(LocalidadesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LocalidadesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LocalidadesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Localidades(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.localidades).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
