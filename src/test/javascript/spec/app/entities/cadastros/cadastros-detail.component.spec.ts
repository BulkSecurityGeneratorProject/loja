/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { LojaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
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
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CadastrosService,
                    JhiEventManager
                ]
            }).overrideTemplate(CadastrosDetailComponent, '')
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

            spyOn(service, 'find').and.returnValue(Observable.of(new Cadastros(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cadastros).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
