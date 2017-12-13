/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { LojaTestModule } from '../../../test.module';
import { ProdutosDialogComponent } from '../../../../../../main/webapp/app/entities/produtos/produtos-dialog.component';
import { ProdutosService } from '../../../../../../main/webapp/app/entities/produtos/produtos.service';
import { Produtos } from '../../../../../../main/webapp/app/entities/produtos/produtos.model';
import { MarcasService } from '../../../../../../main/webapp/app/entities/marcas';
import { CategoriasService } from '../../../../../../main/webapp/app/entities/categorias';
import { CoresService } from '../../../../../../main/webapp/app/entities/cores';
import { TamanhosService } from '../../../../../../main/webapp/app/entities/tamanhos';

describe('Component Tests', () => {

    describe('Produtos Management Dialog Component', () => {
        let comp: ProdutosDialogComponent;
        let fixture: ComponentFixture<ProdutosDialogComponent>;
        let service: ProdutosService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [ProdutosDialogComponent],
                providers: [
                    MarcasService,
                    CategoriasService,
                    CoresService,
                    TamanhosService,
                    ProdutosService
                ]
            })
            .overrideTemplate(ProdutosDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProdutosDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProdutosService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Produtos(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.produtos = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'produtosListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Produtos();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.produtos = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'produtosListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
