/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { LojaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
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
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CadastrosLocalidadesService,
                    JhiEventManager
                ]
            }).overrideTemplate(CadastrosLocalidadesDetailComponent, '')
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

            spyOn(service, 'find').and.returnValue(Observable.of(new CadastrosLocalidades(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cadastrosLocalidades).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
