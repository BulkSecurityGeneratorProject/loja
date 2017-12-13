/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { LojaTestModule } from '../../../test.module';
import { CadastrosLocalidadesDetailComponent } from '../../../../../../main/webapp/app/entities/cadastros-localidades/cadastros-localidades-detail.component';
import { CadastrosLocalidadesService } from '../../../../../../main/webapp/app/entities/cadastros-localidades/cadastros-localidades.service';
import { CadastrosLocalidades } from '../../../../../../main/webapp/app/entities/cadastros-localidades/cadastros-localidades.model';

describe('Component Tests', () => {

    describe('CadastrosLocalidades Management Detail Component', () => {
        let comp: CadastrosLocalidadesDetailComponent;
        let fixture: ComponentFixture<CadastrosLocalidadesDetailComponent>;
        let service: CadastrosLocalidadesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CadastrosLocalidadesDetailComponent],
                providers: [
                    CadastrosLocalidadesService
                ]
            })
            .overrideTemplate(CadastrosLocalidadesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CadastrosLocalidadesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CadastrosLocalidadesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new CadastrosLocalidades(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.cadastrosLocalidades).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
