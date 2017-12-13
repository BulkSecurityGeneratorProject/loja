/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { LojaTestModule } from '../../../test.module';
import { TamanhosComponent } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos.component';
import { TamanhosService } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos.service';
import { Tamanhos } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos.model';

describe('Component Tests', () => {

    describe('Tamanhos Management Component', () => {
        let comp: TamanhosComponent;
        let fixture: ComponentFixture<TamanhosComponent>;
        let service: TamanhosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [TamanhosComponent],
                providers: [
                    TamanhosService
                ]
            })
            .overrideTemplate(TamanhosComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TamanhosComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TamanhosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Tamanhos(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.tamanhos[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
