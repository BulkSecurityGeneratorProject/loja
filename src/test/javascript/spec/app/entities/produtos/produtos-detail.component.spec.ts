/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { LojaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProdutosDetailComponent } from '../../../../../../main/webapp/app/entities/produtos/produtos-detail.component';
import { ProdutosService } from '../../../../../../main/webapp/app/entities/produtos/produtos.service';
import { Produtos } from '../../../../../../main/webapp/app/entities/produtos/produtos.model';

describe('Component Tests', () => {

    describe('Produtos Management Detail Component', () => {
        let comp: ProdutosDetailComponent;
        let fixture: ComponentFixture<ProdutosDetailComponent>;
        let service: ProdutosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [ProdutosDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProdutosService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProdutosDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProdutosDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProdutosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Produtos(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.produtos).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
