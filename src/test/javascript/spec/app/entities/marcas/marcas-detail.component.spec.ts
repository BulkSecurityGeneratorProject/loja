/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { LojaTestModule } from '../../../test.module';
import { MarcasDetailComponent } from '../../../../../../main/webapp/app/entities/marcas/marcas-detail.component';
import { MarcasService } from '../../../../../../main/webapp/app/entities/marcas/marcas.service';
import { Marcas } from '../../../../../../main/webapp/app/entities/marcas/marcas.model';

describe('Component Tests', () => {

    describe('Marcas Management Detail Component', () => {
        let comp: MarcasDetailComponent;
        let fixture: ComponentFixture<MarcasDetailComponent>;
        let service: MarcasService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [MarcasDetailComponent],
                providers: [
                    MarcasService
                ]
            })
            .overrideTemplate(MarcasDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MarcasDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MarcasService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Marcas(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.marcas).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
