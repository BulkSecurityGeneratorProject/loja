/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { LojaTestModule } from '../../../test.module';
import { CadastrosComponent } from '../../../../../../main/webapp/app/entities/cadastros/cadastros.component';
import { CadastrosService } from '../../../../../../main/webapp/app/entities/cadastros/cadastros.service';
import { Cadastros } from '../../../../../../main/webapp/app/entities/cadastros/cadastros.model';

describe('Component Tests', () => {

    describe('Cadastros Management Component', () => {
        let comp: CadastrosComponent;
        let fixture: ComponentFixture<CadastrosComponent>;
        let service: CadastrosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CadastrosComponent],
                providers: [
                    CadastrosService
                ]
            })
            .overrideTemplate(CadastrosComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CadastrosComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CadastrosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Cadastros(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.cadastros[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
