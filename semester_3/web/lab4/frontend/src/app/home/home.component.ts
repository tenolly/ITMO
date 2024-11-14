import { Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, Validators, FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../api.service';

@Component({
    selector: 'home-root',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
    graphForm!: FormGroup;
    xValues: number[] = [-4, -3, -2, -1, 0, 1, 2, 3, 4];
    rValues: number[] = [-4, -3, -2, -1, 0, 1, 2, 3, 4];
    selectedX: number | null = null;
    selectedR: number | null = null;
    results: { x: number; y: number; r: number; hit: boolean, color: string }[] = [];

    constructor(
        private fb: FormBuilder, private apiService: ApiService,
        private el: ElementRef, private renderer: Renderer2
    ) { }

    removePoints() {
        const svg = this.el.nativeElement.querySelector('#graph');
        const circles = svg.querySelectorAll('circle');
        circles.forEach((circle: { remove: () => any; }) => circle.remove());
    }

    drawPoint(x: number, y: number, r: number, result: boolean) {
        const svg = this.el.nativeElement.querySelector('#graph');
        const circle = this.renderer.createElement('circle', 'svg');

        this.renderer.setAttribute(circle, 'cx', `${x * 170 / r + 200}`);
        this.renderer.setAttribute(circle, 'cy', `${-y * 170 / r + 200}`);
        this.renderer.setAttribute(circle, 'r', '4');
        this.renderer.setStyle(circle, 'stroke', 'black');
        this.renderer.setStyle(circle, 'stroke-width', '1px');
        this.renderer.setStyle(circle, 'fill', result ? '#0ecc14' : '#d1220f');

        this.renderer.appendChild(svg, circle);
    }

    validateYValue(y: string): boolean {
        const numY = parseFloat(y);
        return !isNaN(numY) && numY >= -5 && numY <= 3;
    }

    validateRValue(r: number): boolean {
        return !isNaN(r) && r > 0 && r <= 4;
    }

    ngOnInit(): void {
        const dynamicXFields: Record<string, boolean> = {};
        const dynamicRFields: Record<string, boolean> = {};

        this.xValues.forEach(x => {
            dynamicXFields['x' + x] = false;
        });

        this.rValues.forEach(r => {
            dynamicRFields['r' + r] = false;
        });

        this.graphForm = this.fb.group({
            yValue: ['', [Validators.required, Validators.pattern(/^-?[0-9]*\.?[0-9]+$/)]],
            ...dynamicXFields,
            ...dynamicRFields
        });
    }

    onSvgClick(event: MouseEvent) {
        const rValue = this.selectedR;

        if (!rValue || rValue <= 0) {
            alert("Невозможно вычислить попадание");
            return;
        }

        const divElement = document.getElementById('graphContainer') as HTMLElement;
        const divRect = divElement.getBoundingClientRect();
        const x = event.pageX - divRect.left - divRect.width / 2;
        const y = divRect.height / 2 - (event.pageY - divRect.top);

        const calculatedX = (x / 150) * rValue;
        const calculatedY = (y / 150) * rValue;

        this.apiService.check(calculatedX, calculatedY, rValue).subscribe({
            next: (response) => {
                const result = /^true$/i.test(response);
                this.drawPoint(calculatedX, calculatedY, rValue, result);
                this.results.unshift({
                    x: calculatedX,
                    y: calculatedY,
                    r: rValue,
                    hit: result,
                    color: result ? 'green' : 'red'
                });
            },
            error: (error) => {
                alert(error.error);
            }
        });
    }

    onXChange(x: number, event: any): void {
        if (event.target.checked) {
            this.selectedX = x;
        } else {
            this.selectedX = null;
        }
    }

    onRChange(r: number, event: any): void {
        if (event.target.checked) {
            this.selectedR = r;
        } else {
            this.selectedR = null;
        }

        if (this.validateRValue(r)) {
            this.removePoints();

            this.results.forEach(element => {
                this.drawPoint(element.x, element.y, r, element.hit);
            });
        }
    }

    onSubmit(): void {
        const y = this.graphForm.value.yValue;
        const r = this.graphForm.value.rValue;

        if (!this.validateYValue(y)) {
            alert('Введите корректное значение для Y');
            return;
        }

        if (this.selectedR == null || !this.validateRValue(this.selectedR)) {
            alert('Введите корректное значение для R (R должен быть больше 0)');
            return;
        }

        if (this.selectedX !== null && this.selectedR !== null) {
            const x = this.selectedX;
            const yNum = parseFloat(y);
            const r = this.selectedR;
            this.apiService.check(x, yNum, r).subscribe({
                next: (response) => {
                    const result = /^true$/i.test(response);
                    this.drawPoint(x, yNum, r, result);
                    this.results.unshift({
                        x: x,
                        y: parseFloat(y),
                        r: r,
                        hit: result,
                        color: result ? 'green' : 'red'
                    });
                },
                error: (error) => {
                    alert(error.error);
                }
            });
        } else {
            alert('Необходимо выбрать X, Y и R');
        }
    }
}