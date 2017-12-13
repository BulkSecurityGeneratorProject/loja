/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { LojaTestModule } from '../../../test.module';
import { CadastrosDetailComponent } from '../../../../../../main/webapp/app/entities/cadastros/cadastros-detail.component';
import { CadastrosService } from '../../../../../../main/webapp/app/entities/cadastros/cadastros.service';
import { Cadastros } from '../../../../../../main/webapp/app/entities/cadastros/cadastros.model';

describe('Component Tests', () => {

    describe('Cadastros Management Detail Component', () => {
        let comp: CadastrosDetailComponent;
        let fixture: ComponentFixture<CadastrosDetailComponent>;
        let service: CadastrosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CadastrosDetailComponent],
                providers: [
                    CadastrosService
                ]
            })
            .overrideTemplate(CadastrosDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CadastrosDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CadastrosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Cadastros(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.cadastros).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
