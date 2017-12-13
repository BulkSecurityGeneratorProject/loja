/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { PedidosDialogComponent } from '../../../../../../main/webapp/app/entities/pedidos/pedidos-dialog.component';
import { PedidosService } from '../../../../../../main/webapp/app/entities/pedidos/pedidos.service';
import { Pedidos } from '../../../../../../main/webapp/app/entities/pedidos/pedidos.model';
import { CadastrosService } from '../../../../../../main/webapp/app/entities/cadastros';

describe('Component Tests', () => {

    describe('Pedidos Management Dialog Component', () => {
        let comp: PedidosDialogComponent;
        let fixture: ComponentFixture<PedidosDialogComponent>;
        let service: PedidosService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [PedidosDialogComponent],
                providers: [
                    CadastrosService,
                    PedidosService
                ]
            })
            .overrideTemplate(PedidosDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PedidosDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PedidosService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Pedidos(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.pedidos = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'pedidosListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Pedidos();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.pedidos = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'pedidosListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
