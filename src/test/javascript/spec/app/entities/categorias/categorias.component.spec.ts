/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { LojaTestModule } from '../../../test.module';
import { CategoriasComponent } from '../../../../../../main/webapp/app/entities/categorias/categorias.component';
import { CategoriasService } from '../../../../../../main/webapp/app/entities/categorias/categorias.service';
import { Categorias } from '../../../../../../main/webapp/app/entities/categorias/categorias.model';

describe('Component Tests', () => {

    describe('Categorias Management Component', () => {
        let comp: CategoriasComponent;
        let fixture: ComponentFixture<CategoriasComponent>;
        let service: CategoriasService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CategoriasComponent],
                providers: [
                    CategoriasService
                ]
            })
            .overrideTemplate(CategoriasComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CategoriasComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CategoriasService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Categorias(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.categorias[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
