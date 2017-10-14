/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { LojaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
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
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    LocalidadesService,
                    JhiEventManager
                ]
            }).overrideTemplate(LocalidadesDetailComponent, '')
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

            spyOn(service, 'find').and.returnValue(Observable.of(new Localidades(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.localidades).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
