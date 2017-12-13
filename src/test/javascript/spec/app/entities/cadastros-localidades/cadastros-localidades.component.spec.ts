/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { LojaTestModule } from '../../../test.module';
import { CadastrosLocalidadesComponent } from '../../../../../../main/webapp/app/entities/cadastros-localidades/cadastros-localidades.component';
import { CadastrosLocalidadesService } from '../../../../../../main/webapp/app/entities/cadastros-localidades/cadastros-localidades.service';
import { CadastrosLocalidades } from '../../../../../../main/webapp/app/entities/cadastros-localidades/cadastros-localidades.model';

describe('Component Tests', () => {

    describe('CadastrosLocalidades Management Component', () => {
        let comp: CadastrosLocalidadesComponent;
        let fixture: ComponentFixture<CadastrosLocalidadesComponent>;
        let service: CadastrosLocalidadesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CadastrosLocalidadesComponent],
                providers: [
                    CadastrosLocalidadesService
                ]
            })
            .overrideTemplate(CadastrosLocalidadesComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CadastrosLocalidadesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CadastrosLocalidadesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new CadastrosLocalidades(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.cadastrosLocalidades[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
