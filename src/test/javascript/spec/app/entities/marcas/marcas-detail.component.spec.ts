/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { LojaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
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
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MarcasService,
                    JhiEventManager
                ]
            }).overrideTemplate(MarcasDetailComponent, '')
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

            spyOn(service, 'find').and.returnValue(Observable.of(new Marcas(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.marcas).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
