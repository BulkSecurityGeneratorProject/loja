/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { LojaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ItensDetailComponent } from '../../../../../../main/webapp/app/entities/itens/itens-detail.component';
import { ItensService } from '../../../../../../main/webapp/app/entities/itens/itens.service';
import { Itens } from '../../../../../../main/webapp/app/entities/itens/itens.model';

describe('Component Tests', () => {

    describe('Itens Management Detail Component', () => {
        let comp: ItensDetailComponent;
        let fixture: ComponentFixture<ItensDetailComponent>;
        let service: ItensService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [ItensDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ItensService,
                    JhiEventManager
                ]
            }).overrideTemplate(ItensDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ItensDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItensService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Itens(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.itens).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
