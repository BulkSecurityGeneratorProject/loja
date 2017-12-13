/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { ItensDialogComponent } from '../../../../../../main/webapp/app/entities/itens/itens-dialog.component';
import { ItensService } from '../../../../../../main/webapp/app/entities/itens/itens.service';
import { Itens } from '../../../../../../main/webapp/app/entities/itens/itens.model';
import { PedidosService } from '../../../../../../main/webapp/app/entities/pedidos';
import { ProdutosService } from '../../../../../../main/webapp/app/entities/produtos';

describe('Component Tests', () => {

    describe('Itens Management Dialog Component', () => {
        let comp: ItensDialogComponent;
        let fixture: ComponentFixture<ItensDialogComponent>;
        let service: ItensService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [ItensDialogComponent],
                providers: [
                    PedidosService,
                    ProdutosService,
                    ItensService
                ]
            })
            .overrideTemplate(ItensDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ItensDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItensService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Itens(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.itens = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'itensListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Itens();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.itens = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'itensListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
