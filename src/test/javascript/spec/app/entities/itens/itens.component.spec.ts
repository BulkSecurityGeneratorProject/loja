/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { LojaTestModule } from '../../../test.module';
import { ItensComponent } from '../../../../../../main/webapp/app/entities/itens/itens.component';
import { ItensService } from '../../../../../../main/webapp/app/entities/itens/itens.service';
import { Itens } from '../../../../../../main/webapp/app/entities/itens/itens.model';

describe('Component Tests', () => {

    describe('Itens Management Component', () => {
        let comp: ItensComponent;
        let fixture: ComponentFixture<ItensComponent>;
        let service: ItensService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [ItensComponent],
                providers: [
                    ItensService
                ]
            })
            .overrideTemplate(ItensComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ItensComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItensService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Itens(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.itens[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
