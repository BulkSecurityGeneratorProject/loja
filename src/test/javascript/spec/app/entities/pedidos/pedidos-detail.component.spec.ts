/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { LojaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PedidosDetailComponent } from '../../../../../../main/webapp/app/entities/pedidos/pedidos-detail.component';
import { PedidosService } from '../../../../../../main/webapp/app/entities/pedidos/pedidos.service';
import { Pedidos } from '../../../../../../main/webapp/app/entities/pedidos/pedidos.model';

describe('Component Tests', () => {

    describe('Pedidos Management Detail Component', () => {
        let comp: PedidosDetailComponent;
        let fixture: ComponentFixture<PedidosDetailComponent>;
        let service: PedidosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [PedidosDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PedidosService,
                    JhiEventManager
                ]
            }).overrideTemplate(PedidosDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PedidosDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PedidosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Pedidos(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.pedidos).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
