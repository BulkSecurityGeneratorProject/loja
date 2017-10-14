/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { LojaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TamanhosDetailComponent } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos-detail.component';
import { TamanhosService } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos.service';
import { Tamanhos } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos.model';

describe('Component Tests', () => {

    describe('Tamanhos Management Detail Component', () => {
        let comp: TamanhosDetailComponent;
        let fixture: ComponentFixture<TamanhosDetailComponent>;
        let service: TamanhosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [TamanhosDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TamanhosService,
                    JhiEventManager
                ]
            }).overrideTemplate(TamanhosDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TamanhosDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TamanhosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Tamanhos(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.tamanhos).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
